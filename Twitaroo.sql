BEGIN TRANSACTION;

CREATE TABLE Country (
    name VARCHAR(100) NOT NULL PRIMARY KEY
);

CREATE TABLE City (
    cap VARCHAR(8) NOT NULL UNIQUE,
    country VARCHAR(100) NOT NULL,
    name VARCHAR (100) NOT NULL,
    PRIMARY KEY(cap),

    CONSTRAINT country_must_exist FOREIGN KEY (country) 
    REFERENCES Country(name)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
    DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Users(
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    city VARCHAR(8) NOT NULL,
    age NUMERIC NOT NULL,
    password TEXT NOT NULL,
    mail VARCHAR(100) NOT NULL UNIQUE,
    is_profile_public BOOLEAN NOT NULL,
    
    CONSTRAINT valid_mail CHECK (mail ~ '^(.+)@(.+)$'),
    CONSTRAINT valid_age CHECK (age > 13 AND age < 120),

    CONSTRAINT city_must_exist FOREIGN KEY(city)
        REFERENCES City(cap)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
        DEFERRABLE INITIALLY DEFERRED
); 

CREATE TABLE Session(
    session_ID SERIAL,
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME,
    users INTEGER NOT NULL,
    PRIMARY KEY( session_ID,users),

    CONSTRAINT session_must_belong_to_existing_user FOREIGN KEY(users) 
        REFERENCES Users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Tweet(
    tweet_id SERIAL PRIMARY KEY,
    users INTEGER NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    text VARCHAR(255),

    CONSTRAINT tweet_must_belong_to_existing_user FOREIGN KEY(users)
        REFERENCES Users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Phone_number(
    users INTEGER NOT NULL,
    prefix VARCHAR(20) NOT NULL,
    number VARCHAR(20) NOT NULL,
    PRIMARY KEY(prefix,number),

    CONSTRAINT valid_prefix CHECK (prefix ~ '^\+(?:[0-9]?){1,4}'),
    CONSTRAINT valid_number CHECK (number ~ '^[0-9]{7,15}'),

    CONSTRAINT phone_number_must_belong_to_existing_user FOREIGN KEY(users)
        REFERENCES Users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Message(
    message_id SERIAL,
    sender INTEGER NOT NULL,
    receiver INTEGER NOT NULL,
    date DATE NOT NULL,
    time TIME NOT NULL,
    text VARCHAR(255) NOT NULL,
    PRIMARY KEY(message_id,sender,receiver),

    CONSTRAINT sender_must_exist FOREIGN KEY(sender)
        REFERENCES Users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE 
        DEFERRABLE INITIALLY DEFERRED,
    CONSTRAINT receiver_must_exist FOREIGN KEY(receiver)
        REFERENCES Users(user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE Followers(
    follower INTEGER NOT NULL,
    followed INTEGER NOT NULL,

    PRIMARY KEY(follower, followed),

    CONSTRAINT follower_must_exists FOREIGN KEY(follower)
        REFERENCES Users(user_id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED,

    CONSTRAINT followed_user_must_exists FOREIGN KEY(followed)
        REFERENCES Users(user_id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE
        DEFERRABLE INITIALLY DEFERRED
);

-- STORED PROCEDURES

CREATE OR REPLACE FUNCTION is_user_logged_in_tweet()
RETURNS TRIGGER AS $$
DECLARE active_session RECORD;
BEGIN
    SELECT EXISTS(  SELECT * 
                    FROM Session 
                    WHERE users = NEW.users AND start_time IS NOT NULL AND end_time IS NULL AND date = CURRENT_DATE  
                ) INTO active_session;

    IF active_session.exists THEN 
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'You must be logged in to do the requested operation';
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION is_user_logged_in_message()
RETURNS TRIGGER AS $$
DECLARE active_session RECORD;
BEGIN
    SELECT EXISTS(  SELECT * 
                    FROM Session 
                    WHERE users = NEW.sender AND start_time IS NOT NULL AND end_time IS NULL AND date = CURRENT_DATE  
                ) INTO active_session;

    IF active_session.exists THEN 
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'You must be logged in to do the requested operation';
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION verify_time_and_date()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.time > LOCALTIME OR NEW.date > CURRENT_DATE OR NEW.date < CURRENT_DATE THEN 
        RAISE EXCEPTION 'INVALID TIME OR DATE!';
        RETURN NULL;
    ELSE
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION isMessageReceiverFollower()
RETURNS TRIGGER AS $$
DECLARE record RECORD;
BEGIN
    SELECT EXISTS( SELECT * FROM Followers WHERE followed = NEW.sender AND follower = NEW.receiver) INTO record;
    IF record.exists THEN 
        RETURN NEW;
    ELSE
        RETURN NULL;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- TRIGGERS

CREATE TRIGGER check_session_before_sending_tweet
BEFORE INSERT OR UPDATE OR DELETE ON Tweet
FOR EACH ROW EXECUTE PROCEDURE is_user_logged_in_tweet();

CREATE TRIGGER check_session_before_sending_message
BEFORE INSERT OR UPDATE OR DELETE ON Message
FOR EACH ROW EXECUTE PROCEDURE is_user_logged_in_message();

CREATE TRIGGER verify_tweet_time_and_date
BEFORE INSERT ON Tweet
FOR EACH ROW EXECUTE
PROCEDURE verify_time_and_date();

CREATE TRIGGER verify_message_time_and_date
BEFORE INSERT ON Message
FOR EACH ROW EXECUTE
PROCEDURE verify_time_and_date();

CREATE TRIGGER check_if_message_receiver_is_follower
BEFORE INSERT ON Message
FOR EACH ROW EXECUTE
PROCEDURE isMessageReceiverFollower();

-- DEFAULT INSERTS

INSERT INTO Country VALUES
    ('Italy'),
    ('Germany'),
    ('Spain'),
    ('Japan');

INSERT INTO City VALUES
    ('39100', 'Italy', 'Bolzano'),
    ('39012', 'Italy', 'Merano'),
    ('39022', 'Italy', 'Lagundo'),
    ('28759', 'Germany', 'Berlin'),
    ('28309', 'Germany', 'Hamburg'),
    ('28195', 'Germany', 'Bremen'),
    ('28001', 'Spain', 'Madrid'),
    ('08001', 'Spain', 'Barcellona'),
    ('46001', 'Spain', 'Valencia'),
    ('85233', 'Japan', 'Tokyo'),
    ('85234', 'Japan', 'Osaka'),
    ('85232', 'Japan', 'Morioh');

COMMIT TRANSACTION;
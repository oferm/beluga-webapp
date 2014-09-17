CREATE TABLE mule.server (
  id            INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 0, INCREMENT BY 1),
  name          VARCHAR(100) NOT NULL,
  serverAddress VARCHAR(100) NOT NULL,
  prefix        VARCHAR(20)  NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE mule.application (
  id           INTEGER      NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 0, INCREMENT BY 1),
  name         VARCHAR(100) NOT NULL,
  muleServerId INTEGER,
  FOREIGN KEY (muleServerId) REFERENCES mule.server (id),
  PRIMARY KEY (id)
);

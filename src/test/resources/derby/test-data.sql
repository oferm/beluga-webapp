INSERT INTO mule.server (name, serverAddress, prefix)
  VALUES ('Local server', 'localhost', 'Mule.');

INSERT INTO mule.application (name, muleServerId) VALUES ('default', (SELECT
                                                                        id
                                                                      FROM mule.server
                                                                      WHERE name = 'Local server'));

INSERT INTO mule.application (name, muleServerId) VALUES ('beluga-1.0-SNAPSHOT', (SELECT
                                                                                    id
                                                                                  FROM mule.server
                                                                                  WHERE name =
                                                                                        'Local server'));
INSERT INTO mule.application (name, muleServerId) VALUES ('TalentumESB', (SELECT
                                                                            id
                                                                          FROM mule.server
                                                                          WHERE name = 'Local server'));

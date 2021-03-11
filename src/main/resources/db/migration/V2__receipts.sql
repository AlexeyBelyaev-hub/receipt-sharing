DROP TABLE receipt_items;
DROP TABLE receipt;


CREATE TABLE IF NOT EXISTS receipt(
      receipt_id UUID PRIMARY KEY,
      date_time TIMESTAMP NOT NULL,
      receipt_number VARCHAR(50) NOT NULL,
      seller VARCHAR(150) NOT NULL,
      owner_user_id UUID REFERENCES app_user(app_user_id) NOT NULL

);

CREATE TABLE IF NOT EXISTS receipt_items(
      receipt_id UUID REFERENCES receipt(receipt_id) NOT NULL,
      item_id SMALLINT,
      title VARCHAR(150) NOT NULL,
      quantity INTEGER NOT NULL CHECK(quantity>0),
      price DECIMAL(12,2)  NOT NULL CHECK(price>0),
      sum DECIMAL(12,2) NOT NULL CHECK(sum>0),
      PRIMARY KEY(receipt_id, item_id)
);
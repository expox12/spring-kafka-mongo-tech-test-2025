db = db.getSiblingDB('hotel');

db.createUser({
  user: "hotel_user",
  pwd: "hotel_pass",
  roles: [
    { role: "readWrite", db: "hotel" }
  ]
});


db = db.getSiblingDB('hotel_test');

db.createUser({
  user: "hotel_test_user",
  pwd: "hotel_test_pass",
  roles: [
    { role: "readWrite", db: "hotel_test" }
  ]
});

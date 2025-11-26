db = db.getSiblingDB('hotel');

db.createUser({
  user: "hotel_user",
  pwd: "hotel_pass",
  roles: [
    { role: "readWrite", db: "hotel" }
  ]
});

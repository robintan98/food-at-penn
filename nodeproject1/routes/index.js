var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

/* GET register page.
  Router to register page, where users can register accounts
  @developer: Robin
*/
router.get('/register', function(req, res) {
  res.render('register', { title: 'Register'});
});

/* GET accounts page.
  Router to accounts page, where admins can see currently registered accounts
  Note: From non-admin end, should not be able to see current accounts, mainly for
  development purposes
  Extracts db object from request, fill docs variable with accounts data, do page render
  @developer: Robin
*/
router.get('/accounts', function(req, res) {
  var db = req.db;
  var collection = db.get('accountscollection');
  collection.find({}, {}, function(e, docs) {
    res.render('accounts', {
      "accounts" : docs
    });
  });
});

/* POST to Register
  Routes user to homepage after registering an account
  Also inserts account information into MongoDB
  Note: May need to create the 'accountscollection' db beforehand for dev purposes 
  @developer: Robin
*/
router.post('/register', function(req, res) {

  var db = req.db;

  // Attributes = attributes from req.body
  var firstName = req.body.firstname;
  var lastName = req.body.lastname;
  var username = req.body.username;
  var password = req.body.password;

  // Collection name is 'accountscollection'
  var collection = db.get('accountscollection');

  // Check DB for repeated usernames
  collection.find().then(collection => {
    collection.forEach(function(account){
        // If account's username already exists, redirect to error code
        if (account.username == username) {
          res.send("Account already exists");
        }
    });
  })

  // Submit to the DB
  collection.insert({
      "firstname" : firstName,
      "lastname" : lastName, 
      "username" : username,
      "password" : password
  }, function (err, doc) {
      if (err) {
          // If it failed, return error
          res.send("There was a problem adding the information to the database.");
      }
      else {
          // Else, redirect to homepage
          res.redirect("accounts");
      }
  });

});

module.exports = router;

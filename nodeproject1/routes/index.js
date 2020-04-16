var express = require('express');
var router = express.Router();
var current = "";

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

/* GET login page.
*/
router.get('/login', function(req, res) {
  res.render('login', { title: 'Login'});
});

/* GET accounts page.
  Router to accounts page, where admins can see currently registered accounts
  Note: From non-admin end, should not be able to see current accounts, mainly for
  development purposes
  Extracts db object from request, fill docs variable with accounts data, do page render
  @developer: Robin and Hannah
*/
router.get('/accounts', function(req, res) {
	var db1 = req.db1;
	var collection = db1.get('postscollection');
    collection.find({}, {}, function(e, docs) {
    res.render('accounts', {
	  accounts : {},
	  user : current,
      posts : docs,
    });
    });  
});

/* GET accounts page for admin.
  @developer: Hannah
*/
router.get('/accountsAdmin', function(req, res) {
	var db = req.db;
	var collection = db.get('accountscollection');
    collection.find({}, {}, function(e, docs) {
    res.render('accountsAdmin', {
      accounts : docs,
    });
	});
});	

/* GET all posts page.
  @developer: Hannah
*/
router.get('/posts', function(req, res) {
  var db1 = req.db1;
  var collection = db1.get('postscollection');
  collection.find({}, {}, function(e, docs) {
    res.render('posts', {
      "posts" : docs
    });
  });
});

/* GET first graph page.
  @developer: Hannah
*/
router.get('/graphs', function(req, res) {
  var db1 = req.db1;
  var collection = db1.get('postscollection');
  collection.find({}, {}, function(e, docs) {
    res.render('graphs', {
      "graphs" : docs
    });
  });
});

/* GET second graph page.
  @developer: Hannah
*/
router.get('/foodGraph', function(req, res) {
  var db1 = req.db1;
  var collection = db1.get('postscollection');
  collection.find({}, {}, function(e, docs) {
    res.render('foodGraph', {
      "foodGraph" : docs
    });
  });
});


/* POST to Register
  Routes user to dashboard after registering an account
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
    collection.forEach(function(account) {
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
          // Else, redirect to dashboard
		  current = username;
          if (username == "hwang") {
				res.redirect("accountsAdmin");
		} else {
				res.redirect("accounts");
		}
      }
  });

});

/* POST to Login
  Routes user to dashboard after validating username and password
  Redirects to error code if username and password do not match
  Redirects to login if no username is found
  Note: May need to create the 'accountscollection' db beforehand for dev purposes 
  @developer: Robin
*/
router.post('/login', function(req, res) {

  var db = req.db;

  // Attributes = attributes from req.body
  var username = req.body.username;
  var password = req.body.password;

  // Collection name is 'accountscollection'
  var collection = db.get('accountscollection');

  // Check DB to validate username and password
  collection.find().then(collection => {
    collection.forEach(function(account) {
        if (account.username == username) {
          // If username and password does not match with account's password, redirect to error code
          if (account.password != password) {
            res.send("Password is incorrect!");
          } else {
            // Redirect to dashboard if username and password match
			current = username;
			if (username == "hwang") {
				res.redirect("accountsAdmin");
			} else {
				res.redirect("accounts");
			}
            
          }
        }
    });
    // If no username in the collection matches, then redirect to login page
    res.redirect("login");
  })

});

/* Router to from admin page.
  @developer: Hannah
*/
router.post('/accountsAdmin', function(req, res) {
  res.redirect('posts');
});

/* Router to first graph page.
  @developer: Hannah
*/
router.post('/accounts', function(req, res) {
  res.redirect('graphs');

});

/* Router to first graph page from admin page.
  @developer: Hannah
*/
router.post('/posts', function(req, res) {
  res.redirect('graphs');
});

/* Router to second graph page.
  @developer: Hannah
*/
router.post('/graphs', function(req, res) {
  res.redirect('foodGraph');
  
});

module.exports = router;

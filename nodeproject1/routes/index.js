var express = require('express');
var router = express.Router();

// Current user information to display on the accounts page, admin status, and message
var current = "";
var currentFirstName = "";
var currentLastName = "";
var currentEmail = "";
var currentPhone = "";
var currentSchool = "";
var currentYear = "";
var admin = false;
var msg = "";

/* GET home page. */
router.get('/', function(req, res, next) {
   msg = "";
  res.render('index', { title: 'Food at Penn' });
});

/* GET register page.
  Router to register page, where users can register accounts
  @developer: Robin
*/
router.get('/register', function(req, res) {
	msg = "";
  res.render('register', { title: 'Register'});
});

/* GET login page.
  Router to login page, where users can log in with their accounts
  @developer: Robin
*/
router.get('/login', function(req, res) {
	msg = "";
  res.render('login', { title: 'Login'});
});

router.get('/makeAdmin', function(req, res) {
	if (!admin) {
		res.render('adminError');
	} else {
		  res.render('makeAdmin', { message: msg});
	}
});

/* GET accounts page.
  Router to accounts page, where admins can see currently registered accounts
  Note: From non-admin end, should not be able to see current accounts, mainly for
  development purposes
  Extracts db object from request, fill docs variable with accounts data, do page render
  @developer: Robin and Hannah
*/
router.get('/account', function(req, res) {
	msg = "";
  if (admin) {
    res.redirect('admin');
  } else {
    var docs = req.docs;
    res.render('account', {
      user : current,
      posts : docs,
      userFirstName : currentFirstName,
      userLastName : currentLastName,
      userEmail : currentEmail,
      userPhone : currentPhone,
      userSchool : currentSchool,
      userYear : currentYear,
    }); 
  }
});

/* GET accounts page for admin.
  @developer: Hannah
*/
router.get('/admin', function(req, res) {
	msg = "";
	if (!admin) {
		res.render('adminError');
    } else {
	
	const MongoClient = require('mongodb').MongoClient;
  const uri = 'mongodb+srv://hkwang:135790220@postdb-znag1.mongodb.net/test?retryWrites=true&w=majority';
  const client = new MongoClient(uri, { useNewUrlParser: true });

  client.connect(err => {
    var accountsDB = client.db('accountsDB');
    var accountsCollection = accountsDB.collection('accountscollection');

    accountsCollection.find({}).toArray(function(err, docs) {
      res.render('admin', {
        accounts : docs,
      });
    });
  });
	}
  
});	

/* GET all posts page.
  @developer: Hannah
*/
router.get('/posts', function(req, res) {
	msg = "";
	if (!admin) {
		res.render('adminError');		
	} else {
	var docs = req.docs;
  res.render('posts', {
    "posts" : docs
  });
	}
});

/* GET first graph page.
  @developer: Hannah
*/
router.get('/graphs', function(req, res) {
	msg = "";
	var docs = req.docs;
  res.render('graphs', {
    "graphs" : docs
  });
});

/* GET second graph page.
  @developer: Hannah
*/
router.get('/foodGraph', function(req, res) {
	msg = "";
	if (admin) {
			var docs = req.docs;
  res.render('foodGraphAdmin', {
    "foodGraph" : docs
  });	
    } else {
			var docs = req.docs;
  res.render('foodGraph', {
    "foodGraph" : docs
  });
	}

});

/* POST to Register
  Routes user to dashboard after registering an account
  Checks if that username is already registered in the database
  If so, then refreshes register page
  If not, inserts account information into MongoDB
  @developer: Robin
*/
router.post('/register', function(req, res) {

  var firstName = req.body.firstname;
  var lastName = req.body.lastname;
  var username = req.body.username;
  var password = req.body.password;
  var email = req.body.email;
  var phone = req.body.phone;
  var school = req.body.school;
  var year = req.body.year;
  var adminKey = req.body.adminKey;

  var isAdmin = false;

  const MongoClient = require('mongodb').MongoClient;
  const uri = 'mongodb+srv://hkwang:135790220@postdb-znag1.mongodb.net/test?retryWrites=true&w=majority';
  const client = new MongoClient(uri, { useNewUrlParser: true });

  client.connect(err => {
    var accountsDB = client.db('accountsDB');
    var accountsCollection = accountsDB.collection('accountscollection');
    var shouldInsert = true;

    // Check DB for repeated usernames
    accountsCollection.find().toArray(function(err, array) {
      if (err) {
        console.log('Unable to check repeated usernames!');
      } else {
        array.forEach(function(item) {
          if (item.username == username) {
            console.log('Account already exists!');
            shouldInsert = false;
          }
        });
      }
    });

    if (username.length == 0 || password.length == 0) {
      shouldInsert = false;
    }

    // Querying database to register and check repeated accounts is asynchronous
    // As a result, a 1000 ms delay is needed to check the database for repeated accounts,
    // Before the account can be registered
    // Also, adminKey = "350S20-39", then elevate admin privilege for this user
    setTimeout(function() {
      if (!shouldInsert) {
        console.log('redirected!');
        res.redirect('register');
      } else {
        if (adminKey == "350S20-39") {
          isAdmin = true;
        }
        var insertedDoc = {firstName: firstName,
                           lastName: lastName,
                           username: username,
                           password: password,
                           email: email,
                           phone: phone,
                           school: school,
                           year: year,
                           isAdmin: isAdmin,
                           rating: 5,
                           numReviews: 1
                          };
        accountsCollection.insertOne(insertedDoc, function(err) {
          if (err) {
            console.log('Unable to insert document');
          } else {
            console.log('Account registered!');
            res.redirect('login');
          }
        });
      }
    }, 1000);

  });

});

/* POST to Login
  Routes user to dashboard after validating username and password
  Redirects to login page again if username doesn't exist or username and password do not match
  @developer: Robin
*/
router.post('/login', function(req, res) {

  var username = req.body.username;
  var password = req.body.password;

  const MongoClient = require('mongodb').MongoClient;
  const uri = 'mongodb+srv://hkwang:135790220@postdb-znag1.mongodb.net/test?retryWrites=true&w=majority';
  const client = new MongoClient(uri, { useNewUrlParser: true });

  client.connect(err => {
    var accountsDB = client.db('accountsDB');
    var accountsCollection = accountsDB.collection('accountscollection');
    var shouldLogin = false;
    var isAdmin = false;
    var userFirstName = "";
    var userLastName = "";
    var userEmail = "";
    var userPhone = "";
    var userSchool = "";
    var userYear = "";

    // Check DB if username and password align
    // If so, then update userFirstname and isAdmin
    accountsCollection.find().toArray(function(err, array) {
      if (err) {
        console.log('Unable to check is username and password aligns!');
      } else {
        array.forEach(function(item) {
          if (item.username == username) {
            if (item.password == password) {
              shouldLogin = true;
              userFirstName = item.firstName;
              userLastName = item.lastName;
              userEmail = item.email;
              userPhone = item.phone;
              userSchool = item.school;
              userYear = item.year;
              isAdmin = item.isAdmin;
              console.log('Username and password match!');
            } 
          }
        });
      }
    });

    // Querying database to check if username and password matches is asynchronous
    // As a result, a 1000 ms delay is needed to check the database for matching password before the user can login
    // If they don't match, redirects back to login page
    // Also, if user has admin status, then redirect to admin page instead
    setTimeout(function() {
      if (!shouldLogin) {
        console.log('Username and password don\'t match!');
        res.redirect('login');
      } else {
        current = username;
        currentFirstName = userFirstName;
        currentLastName = userLastName;
        currentEmail = userEmail;
        currentPhone = userPhone;
        currentSchool = userSchool;
        currentYear = userYear;
        if (isAdmin) {
          console.log('Redirecting to admin!');
		      admin = true;
          res.redirect('admin');
        } else {
          console.log('Redirecting to account!');
		      admin = false;
          res.redirect('account');
        }
      }
    }, 1000);

  });

});

/* Router to from admin page.
  @developer: Hannah
*/
router.post('/admin', function(req, res) {
  res.redirect('posts');
});

/* Router to first graph page.
  @developer: Hannah
*/
router.post('/account', function(req, res) {
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

/* Router to page for setting admin.
  @developer: Hannah
*/
router.post('/foodGraphAdmin', function(req, res) {
  res.redirect('makeAdmin');
  
});

router.post('/makeAdmin', function(req, res) {

  var userToMake = req.body.userToMake;
  console.log(userToMake);

  const MongoClient = require('mongodb').MongoClient;
  const uri = 'mongodb+srv://hkwang:135790220@postdb-znag1.mongodb.net/test?retryWrites=true&w=majority';
  const client = new MongoClient(uri, { useNewUrlParser: true });

  client.connect(err => {
    var accountsDB = client.db('accountsDB');
    var accountsCollection = accountsDB.collection('accountscollection');
    var accountExists = false;
	  var alreadyAdmin = false;

    // Check DB for user
    accountsCollection.find().toArray(function(err, array) {
      if (err) {
        console.log('Unable to check repeated usernames!');
      } else {
          array.forEach(function(item) {
            if (item.username == userToMake) {
              console.log(item.username);
              console.log(item.isAdmin);
              accountExists = true;
              if (item.isAdmin == true) {
                alreadyAdmin = true;
              }
            }
        });
      }
    });

    // Querying database to register and check repeated accounts is asynchronous
    // As a result, a 1000 ms delay is needed to check the database for repeated accounts,
    // Before the account can be registered
    // Also, adminKey = "350S20-39", then elevate admin privilege for this user
    setTimeout(function() {
      if (!accountExists) {
        msg = "request failed. account does not exist."
        res.redirect('makeAdmin');
      } else if (accountExists && alreadyAdmin) {
		    msg = "request failed. this user is already an admin."
        res.redirect('makeAdmin');
	    }
      else {
        accountsCollection.updateOne({username:userToMake}, {$set: {isAdmin:true}});
        msg = "user successfully made admin";
        res.redirect('makeAdmin');
      }
    }, 1000);
  });

});


module.exports = router;

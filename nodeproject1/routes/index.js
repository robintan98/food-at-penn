var express = require('express');
var router = express.Router();
var current = "";
var currentFirstName = "";

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
  Router to login page, where users can log in with their accounts
  @developer: Robin
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
  var docs = req.docs;
  console.log(docs);
  res.render('accounts', {
    accounts : {},
    user : current,
    posts : docs,
    userFirstName : currentFirstName
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
	var docs = req.docs;
  res.render('posts', {
    "posts" : docs
  });
});

/* GET first graph page.
  @developer: Hannah
*/
router.get('/graphs', function(req, res) {
	var docs = req.docs;
  res.render('graphs', {
    "graphs" : docs
  });
});

/* GET second graph page.
  @developer: Hannah
*/
router.get('/foodGraph', function(req, res) {
	var docs = req.docs;
  res.render('foodGraph', {
    "foodGraph" : docs
  });
});


/* POST to Register
  Routes user to dashboard after registering an account
  Also inserts account information into MongoDB
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

    // Querying database to register and check repeated accounts is asynchronous
    // As a result, a 1000 ms delay is needed to check the database for repeated accounts,
    // Before the account can be registered
    setTimeout(function() {
      if (!shouldInsert) {
        console.log('redirected!');
        res.redirect('register');
      } else {
        var insertedDoc = {firstName: firstName,
                           lastName: lastName,
                           username: username,
                           password: password,
                           email: email,
                           phone: phone,
                           school: school,
                           year: year};
          accountsCollection.insertOne(insertedDoc, function(err){
          if (err) {
            console.log('Unable to insert document');
          } else {
            console.log('Account registered!');
            res.redirect('accounts');
          }
        });
      }
    }, 1000);

  });

});

/* POST to Login
  Routes user to dashboard after validating username and password
  Redirects to error code if username and password do not match
  Redirects to login if no username is found
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
    var userFirstName = "";

    // Check DB if username and password align
    accountsCollection.find().toArray(function(err, array) {
      if (err) {
        console.log('Unable to check is username and password aligns!');
      } else {
        array.forEach(function(item) {
          if (item.username == username) {
            if (item.password == password) {
              shouldLogin = true;
              userFirstName = item.firstName;
              console.log('Username and password match!');
            } 
          }
        });
      }
    });

    // Querying database to check if username and password matches is asynchronous
    // As a result, a 1000 ms delay is needed to check the database for matching password,
    // Before the user can login
    setTimeout(function() {
      if (!shouldLogin) {
        console.log('Username and password don\'t match!');
        res.redirect('login');
      } else {
        current = username;
        currentFirstName = userFirstName;
        console.log('Redirecting to account!');
        res.redirect('accounts');
      }
    }, 1000);

  });

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

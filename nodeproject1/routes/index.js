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

module.exports = router;

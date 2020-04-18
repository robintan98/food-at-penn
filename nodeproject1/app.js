var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var logger = require('morgan');

/* Use the MongoDB using Monk ORM
  Create a Monk connection object
  Monk is used for is simple scope and comprehensibility
  Database is located at localhost:27017/usertest
  Note: May need to download MongoDB and create usertest db branch
  @developer: Robin
  
  Create Monk connection object to db of posts
  Database is located at localhost:27017/posttest
  @developer: Hannah
*/
var monk = require('monk');
var db = monk('localhost:27017/usertest');
const MongoClient = require('mongodb').MongoClient;
const uri = "mongodb+srv://hkwang:135790220@postdb-znag1.mongodb.net/test?retryWrites=true&w=majority";
const client = new MongoClient(uri, { useNewUrlParser: true });
var postDocs = {};
var accountsDB;
// accountsDB.find({}).toArray(function(err, docs) {
//   postDocs = docs;
//   console.log(postDocs);
// });

client.connect(err => {
  var postsDB = client.db('postDB').collection('postscollection');
  postsDB.find({}).toArray(function(err, docs) {
    postDocs = docs;
    console.log(postDocs);
  });

  accountsDB = client.db('accountsDB');

  // accountsDB = client.db('accountsDB').collection('accountscollection');
  // accountsDB.find({}).toArray(function(err, docs) {
  //   postDocs = docs;
  //   console.log(postDocs);
  // });

  console.log('received');
  client.close();
});

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

/* Make db accessible to router
  Must be put above app.use(...indexrouter)
  @developer: Robin
  
  Add post db to accessibility
  @developer: Hannah
*/
app.use(function(req, res, next){
  req.db = db;
  req.accountsDB = accountsDB;
  req.docs = postDocs;
  next();
});

app.use('/', indexRouter);
app.use('/users', usersRouter);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

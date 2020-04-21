var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var logger = require('morgan');

var monk = require('monk');
var db = monk('localhost:27017/usertest');
const MongoClient = require('mongodb').MongoClient;
const uri = "mongodb+srv://dbUser:Food@Penn@cluster0-j6b9f.mongodb.net/test?retryWrites=true&w=majority";
const client = new MongoClient(uri, { useNewUrlParser: true });

var postDocs = {};

client.connect(err => {
  var db1 = client.db('nodelogin').collection('posts');
  db1.find({}).toArray(function(err, docs) {
    postDocs = docs;
    console.log(postDocs);
  });

  console.log('received');
  client.close();
});

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use(function(req, res, next){
  req.db = db;
  req.docs = postDocs;
  next();
});

module.exports = app;

app.use('/test', (req, res) => {
        // create a JSON object
        var data = { 'message' : 'It works!' };
        // send it back
        res.json(data);
    });

//Get all posts from db
app.get('/posts', (request, response, next) => {
    response.json(postDocs);
});

//modify post
app.post('/modifypost', (request, response, next) => {

    var post_data = request.body;

    var id = post_data.id;
    var date = post_data.date;
    var food = post_data.food;
    var description = post_data.description;
    var location = post_data.location;

    postDocs.find({'user' : user}).count(function(err, number) {
        if (number == 0) {
        response.json({'status' : 'user not found'});
        console.log('status: user not found');

        } else {
            postDocs.findOne({'user':user}, function (err, user) {

            var newValues = {$set: {'food':food, 'weekday': weekday, 'time':time, 'location':location}};
            postDocs.updateOne({'user' : user}, newValues, (err, res) => {
                if (err) {
                res.json(err);
                console.log('error');
             } else {
                res.json('Modified Post');
                console.log('Modified Post');


             }
            });
            })
        }
    })
});

// This starts the web server on port 3000.
app.listen(3000, () => {
    console.log('Listening on port 3000');
});
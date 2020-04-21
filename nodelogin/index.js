var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');

//PASSWORD
var genRandomString = function(length) {
	return crypto.randomBytes(Math.ceil(length/2))
		.toString('hex')
		.slice(0, length);
};

var sha512 = function(password, salt) {
	var hash = crypto.createHmac('sha512', salt);
	hash.update(String(password));
	var value = hash.digest('hex');
	return {
		salt:salt,
		passwordHash:value
	};
};

function saltHashPassword(userPassword) {
	var salt = genRandomString(16);
	var passwordData = sha512(userPassword, salt);
	return passwordData;
}

function checkHashPassword(userPassword, salt) {
	var passwordData = sha512(userPassword, salt);
	return passwordData;
}

class Post {
	constructor(date, food, description, location, id, email, comments) {
		this.id = id;
		this.date = date;
		this.food = food;
		this.description = description;
		this.location = location;
		this.email = email;
		this.comments = comments;
	}
}

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

const MongoClient = require('mongodb').MongoClient;
const uri = "mongodb+srv://dbUser:Food@Penn@cluster0-j6b9f.mongodb.net/test?retryWrites=true&w=majority";
const client = new MongoClient(uri, { useNewUrlParser: true });

client.connect( function(err){
	if(err) {
		console.log('Unable to connect to mongoDB server', err);

	} else {

		//Register
		app.post('/register', (request, response, next) => {
			var post_data = request.body;

			var plaint_password = post_data.password;
			var hash_data = saltHashPassword(plaint_password);
			var password = hash_data.passwordHash;
			var salt = hash_data.salt;

			var name = post_data.name;
			var email = post_data.email;
			var year = post_data.year;
			var phone = post_data.phone;

			var insertJson = {
				'email' : email,
				'password': password,
				'salt': salt,
				'name': name,
				'year': year,
				'phone': phone,
				'rating': "5",
				'numReviews': "1"
			};
			var db = client.db('nodelogin');

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number != 0) {

					response.json('Email already exists');
					console.log('Email already exists');

				} else {
					db.collection('user')
						.insertOne(insertJson, function(error, res) {
							response.json('Registered user');
							console.log('Registered user');
						})
				}
			})

		});


		//Login
		app.post('/login', (request, response, next) => {
			var post_data = request.body;


			var email = post_data.email;
			var userPassword = post_data.password;



			var db = client.db('nodelogin');

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number == 0) {

					response.json('Email doesn\'t exist');
					console.log('Email doesn\'t exist');

				} else {
					db.collection('user')
						.findOne({'email':email}, function(err, user) {
							var salt = user.salt;
							var hashedPassword = checkHashPassword(userPassword, salt).passwordHash; //Hash password
							var encrypted_password = user.password; //Get password from user
							if (hashedPassword == encrypted_password) {
								response.json('Login successful');
								console.log('Login successful');
							} else {
								response.json('Wrong password');
								console.log('Wrong password');
							}
						})
				}
			})

		});

		//Contains
		app.post('/contains', (request, response, next) => {
			var post_data = request.body;


			var email = post_data.email;

			var db = client.db('nodelogin');

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number == 0) {

					response.json('false');
					console.log('false');

				} else {
					response.json('true');
					console.log('true');
				}
			})

		});

		//Get User
		app.post('/getUser', (request, response, next) => {
			var post_data = request.body;


			var email = post_data.email;



			var db = client.db('nodelogin');

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number == 0) {

					response.json({'status': 'email not found'});
					console.log('status: email not found');

				} else {
					db.collection('user')
						.findOne({'email':email}, function(err, user) {


							response.json(user);
							console.log('User found');

						})
				}
			})

		});

		//Modify User
		app.post('/modify', (request, response, next) => {
			var post_data = request.body;


			var name = post_data.name;
			var email = post_data.email;
			var year = post_data.year;
			var phone = post_data.phone;

			var db = client.db('nodelogin');

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number == 0) {

					response.json({'status': 'email not found'});
					console.log('status: email not found');

				} else {
					db.collection('user')
						.findOne({'email':email}, function(err, user) {


							var newValues = {$set: {'name': name, 'year': year, 'phone': phone}};
							db.collection('user').updateOne( {'email':email}, newValues, (err, res) => {
								if(err) {
									res.json( 'error' );
									console.log('error');
								} else {
									response.json('Modified User');
									console.log('Modified user');
								}});

						})
				}
			})

		});

		//Get User Names
		app.post('/users', (request, response, next) => {

			var db = client.db('nodelogin');

			db.collection('user').find({}).toArray(function(err, result) {
				console.log(result);
				response.json(result);
			})

		});

		app.post('/createPost', (request, response, next) => {
			var post_data = request.body;
			var db = client.db('nodelogin');

			var id = post_data.id;
			var date = post_data.date;
			var food = post_data.food;
			var description = post_data.description;
			var location = post_data.location;
			var email = post_data.email;
			var comments = post_data.comments;
			

			var newPost = new Post(date, food, description, location, id, email, comments);

			db.collection('posts').find({'id':id}).count(function(err, number) {
				if (number != 0) {

					response.json('Post already exists');
					console.log('Post already exists');

				} else {
					db.collection('posts')
						.insertOne(newPost, function(error, res) {
							response.json('Registered post');
							console.log('Registered post');
						})
				}
			})

		});





		//Set new rating datapoint
		app.post('/addRating', (request, response, next) => {
			var post_data = request.body;
			var db = client.db('nodelogin');

			var email = post_data.email;
			var rating = Number(post_data.rating);

			db.collection('user').find({'email':email}).count(function(err, number) {
				if (number == 0) {

					response.json({'status': 'email not found'});
					console.log('status: email not found');

				} else {
					db.collection('user')
						.findOne({'email':email}, function(err, user) {
							var numSoFar = Number(user['numReviews']);
							var ratingPrev = Number(user['rating']);

							ratingPrev = numSoFar * ratingPrev;
							ratingPrev = ratingPrev + rating;
							numSoFar = numSoFar + 1;
							var newRating = ratingPrev / numSoFar;

							var newValues = {$set: {rating: newRating, numReviews: numSoFar}};

							db.collection('user').updateOne( {'email':email}, newValues, (err, res) => {
								if(err) {
									res.json( 'error' );
									console.log('error');
								} else {
									response.json(newRating);
									console.log(newRating);
								}});


						})
				}
			})

		});



		app.post('/modifyPost', (request, response, next) => {
        	var post_data = request.body;

        	var db = client.db('nodelogin');

            var id = post_data.id;
            var food = post_data.food;
            var description = post_data.description;
            var location = post_data.location;
	    var email = post_data.email;
	    var comments = post_data.comments;


        	db.collection('posts').find({'id':id}).count(function(err, number) {
        		if (number == 0) {

        			response.json({'status': 'email not found'});
        			console.log('status: email not found');

        		} else {
        			db.collection('posts').findOne({'id':id}, function(err, user) {


        			var newValues = {$set: {'food': food, 'description': description, 'location': location, 'email' : email, 'comments' : comments}};
        			db.collection('posts').updateOne( {'id':id}, newValues, (err, res) => {
        				if(err) {
        					res.json( 'error' );
        					console.log('error');
        				} else {
        					response.json('Modified Posts');
        					console.log('Modified posts');
        				}});

        		    })
                 }
            })

        });

        app.post('/deletePost', (request, response, next) => {
            var post_data = request.body;

            var db = client.db('nodelogin');
              var id = post_data.id;



            db.collection('posts').find({'id':id}).count(function(err, number) {
                if (number == 0) {

                    response.json({'status': 'email not found'});
                    console.log('status: email not found');

                } else {
                    db.collection('posts').deleteOne({'id':id}, function(err, user) {

                        if(err) {
                            res.json( 'error' );
                            console.log('error');
                        } else {
                            response.json('Deleted Posts');
                            console.log('Deleted posts');
                        }});

                    }

            })

        });




		//Start web server
		app.listen(3001, () => {
			console.log('Connected to MongoDB Server, WebService on port 3001');
		})
	}
});
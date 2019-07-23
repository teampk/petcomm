var dogdb = require('../models/dog');
var fs = require('fs');

exports.registerDog = (mDogName, mDogGender, mDogSpecies, mDogBirth, mDogWeight, mUserEmail, mFeederId, mToiletId) =>

	new Promise(
		function(resolve,reject){

	 	var newDog = new dogdb({
			dogName: mDogName,
			dogGender: mDogGender,
			dogBreeds: mDogSpecies,
			dogBirth: mDogBirth,
			dogWeight : mDogWeight,
			userEmail : mUserEmail,
			feederId : mFeederId,
			toiletId : mToiletId,
			created_at: new Date()
		});

		newDog.save()
		.then(function(){
			console.log('-- Register Complete --');
			resolve({ status: 201, message: 'Dog Registered Sucessfully !' });
		})
		.catch(function(err){
			if (err.code == 11000) {
				reject({ status: 409, message: 'Dog Already Registered !' });
			} else {
				reject({ status: 500, message: 'Internal Server Error !' });
			}
		});
	});

exports.loadDogs = userId =>
	new Promise(function(resolve,reject){
		dogdb.find({ userEmail: userId })
		.then(function(dogs){
			resolve(dogs);
		})
		.catch(function(err){
			reject({ status: 500, message: 'Internal Server Error !' });
		});

	});

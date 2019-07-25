var dogdb = require('../models/dog');
var util = require('./util.js');
var fs = require('fs');

exports.registerDog = (mDogId, mDogName, mDogGender, mDogSpecies, mDogBirth, mDogWeight, mUserEmail, mFeederId, mToiletId) =>

	new Promise(
		function(resolve,reject){

	 	var newDog = new dogdb({
			dogId : mDogId,
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

exports.getDogsById = userId =>
	new Promise(function(resolve,reject){
		dogdb.find({ userEmail: userId })
		.then(function(dogs){
			resolve(dogs);
		})
		.catch(function(err){
			reject({ status: 500, message: 'Internal Server Error !' });
		});
	});

exports.getDogBydogId = dogId =>
	new Promise(function(resolve, reject){
		dogdb.find({ dogId: dogId })
		.then(function(dogs){
			resolve(dogs[0]);
		})
		.catch(function(err){
			reject({status: 500, message: 'Internal Server Error :('});
		});
	});

exports.setDeviceId = (dogId, deviceMode) =>
	new Promise(function(resolve, reject){
		dogdb.find({ dogId: dogId })
		.then(selectedDog => {
			let dog = selectedDog[0];
			if (deviceMode == 1){
				dog.feederId = 'f_' + util.randomString(6);
			}else if (deviceMode == 2){
				dog.toiletId = 't_' + util.randomString(6);
			}else if (deviceMode == 11){
				dog.feederId = '';
			}else if (deviceMode == 22){
				dog.toiletId = '';
			}
			return dog.save();
		})
		.then(dog => 
			resolve({ 
				status: 200, message: 'Feeder Registered Sucessfully!'
			}))
		.catch(err => 
			reject({ 
				status: 500, message: 'Internal Server Error!'
			}));
	});
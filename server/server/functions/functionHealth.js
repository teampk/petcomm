var eatdb = require('../models/health_eat');
var weightdb = require('../models/health_weight');
var poopdb = require('../models/health_poop'); 

exports.registerHealthEat = (mFeederId, mFeedAmount) =>
    new Promise(
        function(resolve, reject){
            var newEat = new eatdb({
                feederId : mFeederId,
                feedAmount : mFeedAmount,
                created_at : new Date()
            });

            newEat.save()
            .then(function(){
                console.log('-- health data saved --');
                resolve({ status: 201, message: 'Health Eat Data Saved!'});

            })
            .catch(function(err){
                if (err.code == 11000){
                    reject({status:409, message: 'Interval Server Error Code: 409'});
                }else{
                    reject({status:500, message: 'Interval Server Error Code: 500'});

                }
            });
        }
    );

exports.getHealthEatByDeviceId = deviceId =>
	new Promise(function(resolve,reject){
		eatdb.find({ feederId: deviceId })
		.then(function(health_eat){
			resolve(health_eat);
		})
		.catch(function(err){
			reject({ status: 500, message: 'Internal Server Error !' });
		});
	});
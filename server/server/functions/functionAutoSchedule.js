var scheduledb = require('../models/feedSchedule');

exports.removeScheduleByFeederId = feederId =>
    new Promise(
        function(resolve, reject){
            scheduledb.deleteMany({feederId : feederId})
            .then(function(){
                resolve({status:201, message: 'Remove Complete'});
            })
            .catch(function(err){
                if (err.code==10000){
                    reject({status:409, message:'already registered'});
                }else{
                    reject({status:500, message:'Internal Server Error'});
                }
            })
        }
    );

exports.registerSchedule = (mFeederId, mFeedOrder, mFeedTime, mFeedAmount) =>
    new Promise(
        function(resolve, reject){
            var newSchedule = new scheduledb({
                feederId : mFeederId,
                feedOrder : mFeedOrder,
                feedTime : mFeedTime,
                feedAmount : mFeedAmount,
                created_at : new Date()
            });

            newSchedule.save()
            .then(function(){
                console.log('-- Schedule Registered --');
                resolve({ status: 201, message: 'Schedule Registered Sucessfully!'});
            })
            .catch(function(err){
                reject({status:500, message: 'Internal Server Error !'});
            });


        }
    );



exports.getScheduleByFeederId = feederId =>
    new Promise(
        function(resolve, reject){
            scheduledb.find({feederId : feederId})
            .then(function(schedules){
                console.log('-- Getting Schedules... --');
                resolve(schedules);
            })
            .catch(function(err){
                reject({status, message: 'Internal Server Error :('});
            })
        }
    );
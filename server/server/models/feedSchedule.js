var mongoose = require('mongoose');

var feedScheduleSchema = mongoose.Schema({

    //dogId : {type: String, unique: true},
    feederId     : String,
	feedTime   : String,
    feedAmount   : String,
    created_at : String
});

module.exports = mongoose.model('feedSchedule', feedScheduleSchema);

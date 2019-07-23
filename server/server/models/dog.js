var mongoose = require('mongoose');

var Schema = mongoose.Schema;

var dogSchema = mongoose.Schema({

    dogId : {type: String, unique: true},
    dogName     : String,
	dogGender   : String,
	dogBreeds   : String,
    dogBirth    : String,
    dogWeight   : String,
    userEmail       : String,
    feederId    : String,
    toiletId    : String,
    created_at  : String
});

module.exports = mongoose.model('dog', dogSchema);

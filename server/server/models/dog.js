var mongoose = require('mongoose');
var dogSchema = mongoose.Schema({

    dogId : {type: String, unique: true},
    dogName     : String,
	dogGender   : String,
	dogBreeds   : String,
    dogBirth    : String,
    dogWeight   : String,
    userEmail   : String,
    feederId    : String,
    toiletId    : String,
    feederIP    : String,
    toiletIP    : String,
    created_at  : String
});

module.exports = mongoose.model('dog', dogSchema);

var mongoose = require('mongoose');
var healthPoopSchema = mongoose.Schema({

    toiletId : String,
    poopAmount : String,
    created_at  : String
});

module.exports = mongoose.model('health_poop', healthPoopSchema);

const express = require('express')
const router = express.Router()
const User = require('../models/user')
const mongoose = require('mongoose')
const jwt = require('jsonwebtoken')
const db = "mongodb://useradmin:admin123@ds121461.mlab.com:21461/eventsdb"

mongoose.connect(db, err => {
    if(err) {
        console.log("Error!: " + err)
    } else {
        console.log("Connected to the database");
    }
})

function verifyToken(req, res, next) {
    if(!req.headers.authorization) {
        return res.status(401).send('unauthorized request')
    }
	console.log("ver: " + req.headers.authorization);
    let token = req.headers.authorization.split(' ')[1]
	console.log("token: " + token);
    if(token === null) {
        return res.status(401).send('unauthorized request')
    }
    let payload = jwt.verify(token, 'secret')
    if(!payload) {
        return res.status(401).send('unautharized request')
    }
    req.userId = payload.subject
    next()
}

router.get('/', (req, res) => {
    res.send("FROM api route")
})

router.post('/register', (req, res) => {
    let userData = req.body
    let user = new User(userData)
    user.save((error, registeredUser) => {
        if(error) {
            console.log(error)
        }else{
            let payload = {
                subject: registeredUser._id
            }
            let token = jwt.sign(payload, 'secret')
            res.status(200).send({token})
        }
    })
})

router.post('/login', (req,res) => {
    let userData = req.body
    User.findOne({email: userData.email}, (error, user) => {
        if(error) {
            console.log("Error: " + error)
        }else{
            if(!user) {
                res.status(401).send("Invalid email")
            }else{
                if(user.password !== userData.password) {
                    res.status(401).send('!Invalid password')
                }else{
                    let payload = {
                        subject: user._id
                    }
                    let token = jwt.sign(payload, 'secret')
                    res.status(200).send({token})
                }
            }
        }
    })
})

router.get('/events', (req, res) => {
    let events = [
        {
            "_id": "1",
            "name": "Auto export",
            "description": "",
            "date": "2018-01-01"
        },
        {
            "_id": "2",
            "name": "Eet export",
            "description": "Stuff chicken",
            "date": "2018-01-01"
        },
        {
            "_id": "3",
            "name": "Walk the dog",
            "description": "Take the dog for a walk around the lake",
            "date": "2018-01-01"
        },
        {
            "_id": "4",
            "name": "Eutanice the dog",
            "description": "The dog is old take him out back behind the barn and let him have a taste of the shotgun while he's eating his favorite snack",
            "date": "2018-01-01"
        },
        {
            "_id": "5",
            "name": "repair car",
            "description": "Repair the broken engine in the volvo",
            "date": "2018-01-01"
        },
        {
            "_id": "6",
            "name": "Clean the house",
            "description": "Clean the house before the relative arrive!",
            "date": "2018-01-01"
        },
    ]
    res.json(events)
})

router.get('/special', verifyToken ,(req, res) => {
    let events = [
        {
            "_id": "1",
            "name": "Auto export",
            "description": "",
            "date": "2018-01-01"
        },
        {
            "_id": "2",
            "name": "Eet export",
            "description": "Stuff chicken",
            "date": "2018-01-01"
        },
        {
            "_id": "3",
            "name": "Walk the dog",
            "description": "Take the dog for a walk around the lake",
            "date": "2018-01-01"
        },
        {
            "_id": "4",
            "name": "Eutanice the dog",
            "description": "The dog is old take him out back behind the barn and let him have a taste of the shotgun while he's eating his favorite snack",
            "date": "2018-01-01"
        },
        {
            "_id": "5",
            "name": "repair car",
            "description": "Repair the broken engine in the volvo",
            "date": "2018-01-01"
        },
        {
            "_id": "6",
            "name": "Clean the house",
            "description": "Clean the house before the relative arrive!",
            "date": "2018-01-01"
        },
    ]
    res.json(events)
})

module.exports = router

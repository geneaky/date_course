const express = require('express');
const dotenv = require('dotenv');
const morgan = require('morgan');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const session = require('express-session');

dotenv.config();
const app = express();
app.set('port',process.env.PRODUCTION_PORT && process.env.DEVELOPMENT_PORT);

app.use(morgan('dev'));
app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use(cors({origin:true,credentials:true}));
app.use(cookieParser(process.env.SECRET_KEY));
app.use(session({
    saveUninitialized:false,
    resave:false,
    secret:process.env.SECRET_KEY,
    cookie:{
        httpOnly:true,
        secure:false,
    }
}));

app.get('/',(req,res,next)=>{
    res.send('date course start');
})

module.exports = app;
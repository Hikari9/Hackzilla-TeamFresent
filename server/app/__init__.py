from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from werkzeug.utils import secure_filename

app = Flask(__name__)
app.config.from_object( "config" )

db = SQLAlchemy( app )

from app import views, models
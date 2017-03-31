import os
basedir = os.path.abspath( os.path.dirname( __file__ ) )

SQLALCHEMY_DATABASE_URI = "sqlite:///" + os.path.join( basedir, "app.db" )
SQLALCHEMY_MIGRATE_REPO = os.path.join( basedir, "db_repository" )

FILE_UPLOAD_FOLDER = os.path.join( basedir, "files" )
IMAGE_UPLOAD_FOLDER = os.path.join( basedir, "files/images" )
IMAGE_ALLOWED_EXTENSIONS = set( ["png", "jpg", "jpeg"] )
CLASSIFIER_UPLOAD_FOLDER = os.path.join( basedir, "files/classfiers" )
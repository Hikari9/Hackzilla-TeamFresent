import os, uuid
from app import app, db
from flask import request, jsonify
from werkzeug.utils import secure_filename
from .models import Student, Classroom

@app.route( "/classroom/<classroom_id>", methods = ["GET"] )
def get_classroom( classroom_id ):
	classroom_query = Classroom.query.filter_by( id = classroom_id )
	if classroom_query.count() > 0:
		name = classroom_query.first().name
		students = classroom_query.first().get_students()
		return jsonify( { "id": classroom_id, "name": name, "students": students } )
	return jsonify( {} )

@app.route( "/classifier/<student_id>", methods = ["GET"] )
def get_classifier( student_id ):		#TO DO
	student_query = Student.query.filter_by( id = student_id )
	if student_query.count() > 0:
		student = student_query.first()
		return student.id + " " + student.first_name
	return jsonify( {} )

@app.route( "/send_nudes", methods = ["POST"] )
def post_nudes():
	student_id = request.form["student_id"]
	file = request.form["file"]
	
	target = open( os.path.join( app.config["IMAGE_UPLOAD_FOLDER"], student_id + "/" + str( uuid.uuid4() ) + ".jpg" ), "w" )
	target.truncate()
	
	target.write( file )
	target.close()
	
	return student_id + " uploaded an image."

@app.route( "/tmp/add_classroom", methods = ["POST"] )
def add_classroom():
	name = request.form["name"]
	course_code = request.form["course_code"]
	section = request.form["section"]
	school_year = request.form["school_year"]
	school_term = request.form["school_term"]
	
	c = Classroom( name = name, course_code = course_code, section = section, school_year = school_year, school_term = school_term )
	db.session.add( c )
	db.session.commit()
	return c.course_code + " " + c.name

@app.route( "/tmp/add_student", methods = ["POST"] )
def add_student():
	id = request.form["id"]
	first_name = request.form["first_name"]
	middle_name = request.form["middle_name"]
	last_name = request.form["last_name"]
	
	s = Student( id = id, first_name = first_name, middle_name = middle_name, last_name = last_name )
	db.session.add( s )
	db.session.commit()
	
	os.makedirs( os.path.join( app.config["IMAGE_UPLOAD_FOLDER"], id + "/" ) )
	
	return s.first_name

@app.route( "/tmp/enroll", methods = ["POST"] )
def enroll():
	student_id = request.form["student_id"]
	classroom_id = request.form["classroom_id"]
	
	s_query = Student.query.filter_by( id = student_id )
	if s_query.count() < 1:
		return "Student not found. Can't enroll."
	s = s_query.first()
	c_query = Classroom.query.filter_by( id = classroom_id )
	if c_query.count() < 1:
		return "Classroom not found. Can't enroll."
	c = c_query.first()
	
	s.enroll( c )
	db.session.commit()
	return "Student enrolled!"

@app.route( "/tmp/delete", methods = ["POST", "GET"] )
def delete_all():
	students = Student.query.all()
	for s in students:
		db.session.delete( s )
	classrooms = Classroom.query.all()
	for c in classrooms:
		db.session.delete( c )
	db.session.commit()
	return "DELETED EVERYTHAAAANG~!"
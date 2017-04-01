import os, uuid
from app import app, db
from config import FILE_UPLOAD_FOLDER, IMAGE_UPLOAD_FOLDER, CLASSIFIER_UPLOAD_FOLDER
from flask import request, jsonify, send_file, send_from_directory
from .models import Student, Classroom

@app.route( "/classroom/<classroom_id>", methods = ["GET"] )
def get_class( classroom_id ):
	classroom_query = Classroom.query.filter_by( id = classroom_id )
	if classroom_query.count() > 0:
		name = classroom_query.first().name
		students = classroom_query.first().get_students()
		return jsonify( { "id": classroom_id, "name": name, "students": students } )
	return jsonify( {} )

@app.route( "/classifier/<student_id>", methods = ["GET"] )
def get_classifier( student_id ):		#TO DO
	student_query = Student.query.filter_by( id = student_id )
	'''if student_query.count() > 0:
		student = student_query.first()
		return send_file( os.path.join( CLASSIFIER_UPLOAD_FOLDER, str( student.id ) + ".xml" ) )'''
	return jsonify( {} )

@app.route( "/send_nudes", methods = ["POST"] )
def post_nudes():
	student_id = request.form["student_id"]
	file = request.form["file"]
	
	filename = str( uuid.uuid4() + ".jpg"
	
	target = open( os.path.join( IMAGE_UPLOAD_FOLDER, student_id + "/" + filename, "w" )
	target.truncate()
	
	target.write( file )
	target.close()
	
	
	
	return student_id + " uploaded an image."

''' TEMPORARY ROUTES '''

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
	return jsonify( { "name": name, "course_code": course_code, "section": section, "school_year": school_year, "school_term": school_term } )

@app.route( "/tmp/add_student", methods = ["POST"] )
def add_student():
	id = request.form["id"]
	first_name = request.form["first_name"]
	middle_name = request.form["middle_name"]
	last_name = request.form["last_name"]
	
	s = Student( id = id, first_name = first_name, middle_name = middle_name, last_name = last_name )
	db.session.add( s )
	db.session.commit()
	
	new_path = os.path.join( IMAGE_UPLOAD_FOLDER, id + "/" )
	if not os.path.exists( new_path ):
		os.makedirs( new_path )
	
	return jsonify( { "id": id, "first_name": first_name, "middle_name": middle_name, "last_name": last_name } )

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

@app.route( "/tmp/get_student/<id>", methods = ["GET"] )
def get_student( id ):
	student_query = Student.query.filter_by( id = id )
	if student_query.count() > 0:
		student = student_query.first()
		return jsonify( { "id": student.id, "first_name": student.first_name, "middle_name": student.middle_name, "last_name": student.last_name } )

@app.route( "/tmp/get_classrom/<id>", methods = ["GET"] )
def get_classroom( id ):
	classroom_query = Classroom.query.filter_by( id = id )
	if classroom_query.count() > 0:
		classroom = classroom_query.first()
		return jsonify( { "id": classroom.id, "name": classroom.name, "course_code": classroom.course_code, "section": classroom.section, "school_year": classroom.school_year, "school_term": classroom.school_term } )

@app.route( "/tmp/get_all_students", methods = ["POST", "GET"] )
def get_all_students():
	students = []
	student_query = Student.query.all()
	for s in student_query:
		students.append( { "id": s.id, "first_name": s.first_name, "middle_name": s.middle_name, "last_name": s.last_name } )
	return jsonify( students )

@app.route( "/tmp/get_all_classrooms", methods = ["POST", "GET"] )
def get_all_classrooms():
	classrooms = []
	classroom_query = Classroom.query.all()
	for c in classroom_query:
		classrooms.append( { "id": c.id, "name": c.name, "course_code": c.course_code, "section": c.section, "school_year": c.school_year, "school_term": c.school_term } )
	return jsonify( classrooms )
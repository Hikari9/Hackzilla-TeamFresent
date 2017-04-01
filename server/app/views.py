import os, uuid
from app import app, db
from config import FILE_UPLOAD_FOLDER, IMAGE_UPLOAD_FOLDER, CLASSIFIER_UPLOAD_FOLDER, CLASSROOM_UPLOAD_FOLDER, TMP_UPLOAD_FOLDER
from flask import request, jsonify, send_file, send_from_directory
from .models import Student, Classroom

@app.route( "/classroom/<classroom_id>", methods = ["GET"] )
def get_class( classroom_id ):
	classroom_query = Classroom.query.filter_by( id = classroom_id )
	if classroom_query.count() > 0:
		return jsonify( classroom_query.first().get_class() )
	return jsonify( {} )
'''
@app.route( "/classifier/<student_id>", methods = ["GET"] )
def get_classifier( student_id ):		#TO DO
	student_query = Student.query.filter_by( id = student_id )
	if student_query.count() > 0:
		student = student_query.first()
		return send_file( os.path.join( CLASSIFIER_UPLOAD_FOLDER, str( student.id ) + ".xml" ) )
	return jsonify( {} )
'''
@app.route( "/send_class_photo", methods = ["POST"] )
def post_photo():
	classroom_id = request.form["classroom_id"]
	file = request.form["file"]

	target = open( os.path.join( CLASSROOM_UPLOAD_FOLDER, classroom_id + ".jpg" ), "w" )
	target.truncate()

	target.write( file )
	target.close()

	# run face detection and recognition, will return object stuff to jsonify

	return str( classroom_id ) + " uploaded an image."
'''
@app.route( "/send_nudes", methods = ["POST"] )
def post_nudes():
	student_id = request.form["student_id"]
	file = request.form["file"]

	filename = str( uuid.uuid4() ) + ".jpg"

	target = open( os.path.join( IMAGE_UPLOAD_FOLDER, student_id + "/" + filename ), "w" )
	target.truncate()

	target.write( file )
	target.close()

	return student_id + " uploaded an image."
'''
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

	'''new_path = os.path.join( CLASSROOM_UPLOAD_FOLDER, course_code + "/" )
	if not os.path.exists( new_path ):
		os.makedirs( new_path )'''

	return jsonify( c.get_classroom() )

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

	return jsonify( s.get_student() )

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
		return  jsonify( student_query.first().get_student() )

@app.route( "/tmp/get_classrom/<id>", methods = ["GET"] )
def get_classroom( id ):
	classroom_query = Classroom.query.filter_by( id = id )
	if classroom_query.count() > 0:
		return jsonify( classroom_query.first().get_classroom() )

@app.route( "/tmp/get_all_students", methods = ["POST", "GET"] )
def get_all_students():
	students = []
	student_query = Student.query.all()
	for s in student_query:
		students.append( s.get_student() )
	return jsonify( students )

@app.route( "/tmp/get_all_classrooms", methods = ["POST", "GET"] )
def get_all_classrooms():
	classrooms = []
	classroom_query = Classroom.query.all()
	for c in classroom_query:
		classrooms.append( c.get_classroom() )
	return jsonify( classrooms )

@app.route( "/recognize", methods = ["POST"])
def recognize():

	image = request.files["file"]
	print(image)

	filename = os.path.join( TMP_UPLOAD_FOLDER, str(uuid.uuid4()) + ".jpg" )
	target = open( filename, "w" )
	target.truncate()

	target.write( image )
	target.close()

	from ai.fresent.predict import predict_all_faces
	predictions = predict_all_faces(os.path.abspath(filename), 'models/model.xml')
	# {id: [(x, y, w, h), confidence]}
	results = []
	for id, value in predictions.items():
		rect, confidence = value
		x, y, w, h = rect
		student = Student.query.filter_by( id = id ).first()
		results.append({
			"student": student,
			"rect": [x, y, w, h],
			"conf": confidence
		})

	target.delete()
	return jsonify(results)

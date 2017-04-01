import os
from app import db
from config import FILE_UPLOAD_FOLDER, IMAGE_UPLOAD_FOLDER, CLASSIFIER_UPLOAD_FOLDER
from flask import send_file

student_enrollment = db.Table( "student_enrollment", db.Column( "student_id", db.Integer, db.ForeignKey( "student.id" ) ), db.Column( "classroom_id", db.Integer, db.ForeignKey( "classroom.id" ) ) )

class Student( db.Model ):
	id = db.Column( db.Integer, primary_key = True )
	first_name = db.Column( db.String( 50 ) )
	middle_name = db.Column( db.String( 20 ) )
	last_name = db.Column( db.String( 20 ) )

	def enroll( self, classroom ):
		if not self.is_enrolled( classroom ):
			self.classrooms.append( classroom )
			return self
	
	def unenroll( self, classroom ):
		if self.is_enrolled( classroom ):
			self.classrooms.remove( classroom )
			return self
	
	def is_enrolled( self, classroom ):
		return self.classrooms.filter( student_enrollment.c.classroom_id == classroom.id ).count() > 0
	
	def __repr__( self ):
		return "<Student %r %s, %s %s>" % ( self.id, self.last_name, self.first_name, self.middle_name )

class Classroom( db.Model ):
	id = db.Column( db.Integer, primary_key = True )
	name = db.Column( db.String( 20 ) )
	course_code = db.Column( db.String( 10 ), unique = True )
	section = db.Column( db.String( 10 ) )
	school_year = db.Column( db.Integer )
	school_term = db.Column( db.Integer )
	
	students = db.relationship( "Student", secondary = student_enrollment, backref = db.backref( "classrooms", lazy = "dynamic" ) )
	
	def enroll( self, student ):
		if not self.is_enrolled( student ):
			self.students.append( student )
			return self
	
	def unenroll( self, student ):
		if self.is_enrolled( student ):
			self.students.remove( student )
			return self
	
	def is_enrolled( self, student ):
		return self.students/filter( student_enrollment.c.student_id == student.id ).count() > 0
	
	def get_students( self ):
		students = []
		for student in self.students:
			#student_picture = send_file( os.path.join( IMAGE_UPLOAD_FOLDER, str(student.id ) + "/1.jpg" ) )
			student_picture = None
			classifier_url = None
			students.append( { "id": student.id, "first_name": student.first_name, "middle_name": student.middle_name, "last_name": student.last_name, "id_picture": student_picture, "classifier_url": classifier_url } )
		return students
	
	def __repr__( self ):
		return "<Class %s-%s: %s\t%r %r" % ( self.course_code, self.section, self.name, self.school_year, self.school_term )

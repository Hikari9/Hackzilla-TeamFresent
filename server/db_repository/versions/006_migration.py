from sqlalchemy import *
from migrate import *


from migrate.changeset import schema
pre_meta = MetaData()
post_meta = MetaData()
G = Table('G', pre_meta,
    Column('id', INTEGER, primary_key=True, nullable=False),
    Column('course_code', VARCHAR(length=10)),
    Column('name', VARCHAR(length=20)),
    Column('school_term', INTEGER),
    Column('school_year', INTEGER),
    Column('section', VARCHAR(length=10)),
)

classroom = Table('classroom', post_meta,
    Column('id', Integer, primary_key=True, nullable=False),
    Column('name', String(length=20)),
    Column('course_code', String(length=10)),
    Column('section', String(length=10)),
    Column('school_year', Integer),
    Column('school_term', Integer),
)

student_enrollment = Table('student_enrollment', post_meta,
    Column('student_id', Integer),
    Column('classroom_id', Integer),
)


def upgrade(migrate_engine):
    # Upgrade operations go here. Don't create your own engine; bind
    # migrate_engine to your metadata
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    pre_meta.tables['G'].drop()
    post_meta.tables['classroom'].create()
    post_meta.tables['student_enrollment'].create()


def downgrade(migrate_engine):
    # Operations to reverse the above upgrade go here.
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    pre_meta.tables['G'].create()
    post_meta.tables['classroom'].drop()
    post_meta.tables['student_enrollment'].drop()

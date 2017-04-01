from sqlalchemy import *
from migrate import *


from migrate.changeset import schema
pre_meta = MetaData()
post_meta = MetaData()
G = Table('G', post_meta,
    Column('id', Integer, primary_key=True, nullable=False),
    Column('name', String(length=20)),
    Column('course_code', String(length=10)),
    Column('section', String(length=10)),
    Column('school_year', Integer),
    Column('school_term', Integer),
)


def upgrade(migrate_engine):
    # Upgrade operations go here. Don't create your own engine; bind
    # migrate_engine to your metadata
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    post_meta.tables['G'].columns['course_code'].create()
    post_meta.tables['G'].columns['name'].create()
    post_meta.tables['G'].columns['school_term'].create()
    post_meta.tables['G'].columns['school_year'].create()
    post_meta.tables['G'].columns['section'].create()


def downgrade(migrate_engine):
    # Operations to reverse the above upgrade go here.
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    post_meta.tables['G'].columns['course_code'].drop()
    post_meta.tables['G'].columns['name'].drop()
    post_meta.tables['G'].columns['school_term'].drop()
    post_meta.tables['G'].columns['school_year'].drop()
    post_meta.tables['G'].columns['section'].drop()

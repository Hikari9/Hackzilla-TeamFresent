from sqlalchemy import *
from migrate import *


from migrate.changeset import schema
pre_meta = MetaData()
post_meta = MetaData()
student = Table('student', pre_meta,
    Column('id', INTEGER, primary_key=True, nullable=False),
    Column('first_name', VARCHAR(length=50)),
    Column('middle_name', VARCHAR(length=20)),
    Column('last_name', VARCHAR(length=20)),
    Column('id_picture', VARCHAR(length=50)),
    Column('images_folder', VARCHAR(length=50)),
    Column('classifier_url', VARCHAR(length=50)),
)


def upgrade(migrate_engine):
    # Upgrade operations go here. Don't create your own engine; bind
    # migrate_engine to your metadata
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    pre_meta.tables['student'].columns['classifier_url'].drop()
    pre_meta.tables['student'].columns['id_picture'].drop()
    pre_meta.tables['student'].columns['images_folder'].drop()


def downgrade(migrate_engine):
    # Operations to reverse the above upgrade go here.
    pre_meta.bind = migrate_engine
    post_meta.bind = migrate_engine
    pre_meta.tables['student'].columns['classifier_url'].create()
    pre_meta.tables['student'].columns['id_picture'].create()
    pre_meta.tables['student'].columns['images_folder'].create()

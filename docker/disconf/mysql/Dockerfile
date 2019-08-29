
# Docker image of disconf mysql
# VERSION 0.0.1
# Author: uniquedawn

FROM mysql:5.5

MAINTAINER uniquedawn <dawnfarrel@126.com>

ENV WORK_PATH /usr/local/work

ENV AUTO_RUN_DIR /docker-entrypoint-initdb.d

ENV FILE_0 0-init_table.sql
ENV FILE_1 1-init_data.sql
ENV FILE_2 20151225.sql
ENV FILE_3 20160701.sql

ENV INSTALL_DATA_SHELL install_data.sh

RUN mkdir -p $WORK_PATH

COPY ./$FILE_0 $WORK_PATH/
COPY ./$FILE_1 $WORK_PATH/
COPY ./$FILE_2 $WORK_PATH/
COPY ./$FILE_3 $WORK_PATH/

COPY ./$INSTALL_DATA_SHELL $AUTO_RUN_DIR/

RUN chmod a+x $AUTO_RUN_DIR/$INSTALL_DATA_SHELL

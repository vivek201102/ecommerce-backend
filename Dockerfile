FROM ubuntu:latest
LABEL authors="vivek"

ENTRYPOINT ["top", "-b"]
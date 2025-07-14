
VERSION=$(shell cat ../version.txt)

all: rest_img xcode_img

xcode_img: base_img Dockerfile_xcode
	set -ex
	docker build -f Dockerfile_xcode . \
		-t xcode_image:latest
	docker image tag xcode_image:latest xcode_image:${VERSION}


rest_img: base_img Dockerfile_rest
	set -ex
	docker build -f Dockerfile_rest . \
		--build-arg BASE_VERSION=${VERSION} \
		-t rest_image:latest
	docker image tag rest_image:latest rest_image:${VERSION}


base_img: Dockerfile_base
	set -ex
	tar -cpf src.tar ../src
	docker build -f Dockerfile_base . \
		-t base_image:latest
	docker image tag base_image:latest base_image:${VERSION}


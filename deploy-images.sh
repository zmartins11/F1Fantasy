#!/bin/bash

EC2_IP_ADDRESS="ipAmazonEc2"

# Function to delete Docker image on EC2 instance
delete_docker_image_ec2() {
    # SSH into EC2 instance and delete Docker image
    ssh -i /home/daniel/Downloads/spotify-key.pem ec2-user@"$EC2_IP_ADDRESS" "sudo docker image rm $1"
    echo "Deleted existing image on EC2 instance: $1"
}

# Function to copy image tar file to EC2 instance
copy_image_tar_to_ec2() {
    # Copy image tar file to EC2 instance
    sudo scp -i /home/daniel/Downloads/spotify-key.pem $1.tar ec2-user@"$EC2_IP_ADDRESS":~/code
    echo "Copied $1.tar to EC2 instance"
}

# Function to load image on EC2 instance
load_image_on_ec2() {
    # SSH into EC2 instance and load image
    ssh -i /home/daniel/Downloads/spotify-key.pem ec2-user@"$EC2_IP_ADDRESS" "sudo docker load -i ~/code/$1.tar && sudo rm ~/code/$1.tar"
    echo "Loaded $1.tar on EC2 instance and removed the tar file"
}

load_docker_compose_on_ec2() {
    # SSH into EC2 instance and run Docker Compose
    ssh -i /home/daniel/Downloads/spotify-key.pem ec2-user@"$EC2_IP_ADDRESS" "cd ~/code && sudo docker-compose up -d"
    echo "Loaded Docker Compose file on EC2 instance"
}



# Process the arguments
for arg in "$@"
do
    case $arg in
        angular)
            # Remove existing Angular image
            sudo docker image rm angularf1-image
            sudo rm angularf1-image.tar

            # Build Angular image
            cd fantasyAngular/fantasyAngular || exit
            sudo docker build -t angularf1-image .
            cd ../../ 

            # Save Angular image to tar file
            sudo docker save -o angularf1-image.tar angularf1-image
            
            delete_docker_image_ec2 angularf1-image
            copy_image_tar_to_ec2 angularf1-image
            load_image_on_ec2 angularf1-image
            ;;
        spring-boot)
            # Remove existing Spring Boot image
            sudo docker image rm springf1-image
            sudo rm springf1-image.tar

            # Build Spring Boot image
            cd demo/ || exit
            mvn clean install -DskipTests
            sudo docker build -t springf1-image .
            cd ../ 

            # Save Spring Boot image to tar file
            sudo docker save -o springf1-image.tar springf1-image
            
            delete_docker_image_ec2 springf1-image
            copy_image_tar_to_ec2 springf1-image
            load_image_on_ec2 springf1-image
            ;;
        *)
            echo "Invalid argument: $arg"
            ;;
    esac
done

# Load Docker Compose file on EC2 instance
load_docker_compose_on_ec2



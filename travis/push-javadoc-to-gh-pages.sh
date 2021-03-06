#!/bin/bash
invokeJavadoc=false
invokeDoc=false

# Only invoke the javadoc deployment process
# for the first job in the build matrix, so as
# to avoid multiple deployments.

if [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then
  case "${TRAVIS_JOB_NUMBER}" in
       *\.1) 
        echo -e "Invoking auto-doc deployment for Travis job ${TRAVIS_JOB_NUMBER}"
        invokeJavadoc=true;
        invokeDoc=true;;
  esac
fi

echo -e "Starting with project documentation...\n"

if [ "$invokeDoc" == true ]; then

  echo -e "Copying project documentation over to $HOME/docs-latest...\n"
  cp -R cas-server-documentation $HOME/docs-latest

fi

echo -e "Finished with project documentation...\n"

echo -e "Staring with project Javadocs...\n"

if [ "$invokeJavadoc" == true ]; then

  echo -e "Started to publish latest Javadoc to gh-pages...\n"

  echo -e "Invoking Maven to generate the project site...\n"
  ./gradlew javadoc -q
  
  echo -e "Copying the generated docs over...\n"
  cp -R build/javadoc $HOME/javadoc-latest

fi

echo -e "Finished with project Javadocs...\n"

if [[ "$invokeJavadoc" == true || "$invokeDoc" == true ]]; then

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  echo -e "Cloning the gh-pages branch...\n"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/Jasig/cas gh-pages > /dev/null

  cd gh-pages

  echo -e "Staring to move project documentation over...\n"

  if [ "$invokeDoc" == true ]; then
    echo -e "Removing previous documentation from development...\n"
    git rm -rf ./development > /dev/null

    echo -e "Creating development directory...\n"
    test -d "./development" || mkdir -m777 -v ./development

    echo -e "Copying new docs from $HOME/docs-latest over to development...\n"
    cp -Rf $HOME/docs-latest/* ./development
    echo -e "Copied project documentation...\n"
  fi

  echo -e "Staring to move project Javadocs over...\n"

  if [ "$invokeJavadoc" == true ]; then
    echo -e "Removing previous Javadocs from /development/javadocs...\n"
    git rm -rf ./development/javadocs > /dev/null

    echo -e "Creating development directory...\n"
    test -d "./development" || mkdir -m777 -v ./development

    echo -e "Creating Javadocs directory at /development/javadocs...\n"
    test -d "./development/javadocs" || mkdir -m777 -v ./development/javadocs

    echo -e "Copying new Javadocs...\n"
    cp -Rf $HOME/javadoc-latest/* ./development/javadocs
    echo -e "Copied project Javadocs...\n"

  fi

  echo -e "Adding changes to the git index...\n"
  git add -f . > /dev/null

  echo -e "Committing changes...\n"
  git commit -m "Published documentation to [gh-pages]. Build $TRAVIS_BUILD_NUMBER" > /dev/null

  echo -e "Pushing upstream to origin...\n"
  git push -fq origin gh-pages > /dev/null

  echo -e "Successfully published documenetation to [gh-pages] branch.\n"

fi

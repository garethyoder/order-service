build-OrderServiceTestFunctionUberJar:
	@ ./gradlew buildAws
	@ mkdir -p $(ARTIFACTS_DIR)/lib
	@ cp  ./build/libs/order-service*.jar $(ARTIFACTS_DIR)/lib/
#	@ cp  ./build/libs/HelloWorld*.jar $(ARTIFACTS_DIR)/lib/
//
// Created by Tusse on 25/08/2018.
//

#include <gtest/gtest.h>
#include <RequestCIMP.h>

using namespace std;

class RequestCIMPTests : public testing::Test
{
public:
    RequestCIMPTests() : _request(";", "#")
    {}

protected:
    RequestCIMP _request;
};

TEST_F(RequestCIMPTests, CreationLoginRequest_RequestIsCreated)
{
    string requestCreated(_request.CreateLoginRequest("Tusset", "123"));

    string requestExcpeted = "1;Tusset;123#";

    ASSERT_EQ(requestExcpeted, requestCreated);
}

TEST_F(RequestCIMPTests, AnalyseRequest_RightTypeRequestTraited)
{
    string requestExcpeted = "1;Tusset;123";

    ASSERT_EQ(1, _request.AnalyseRequest((char*)requestExcpeted.c_str()));
}

TEST_F(RequestCIMPTests, ProcessLoginRequest_RequestProcessed)
{
    ASSERT_EQ(1,  _request.AnalyseRequest(_request.CreateLoginRequest("Tusset", "123")));
}
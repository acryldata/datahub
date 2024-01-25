# Copyright 2021 Acryl Data, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
import os

from aws_msk_iam_sasl_signer import MSKAuthTokenProvider
from base_oauth_cb_provider import BaseOAuthCbProvider


class AwsMskIamSaslSignerCbProvider(BaseOAuthCbProvider):
    @staticmethod
    def oauth_cb(oauth_config):
        aws_region = os.environ.get("AWS_REGION")
        aws_debug_creds = os.environ.get("AWS_DEBUG_CREDS").lower() == "true"
        auth_token, expiry_ms = MSKAuthTokenProvider.generate_auth_token(aws_region, aws_debug_creds=aws_debug_creds)
        # Note that this library expects oauth_cb to return expiry time in seconds since epoch, while the token
        # generator returns expiry in ms
        return auth_token, expiry_ms / 1000


/**
 * @license
 * Copyright 2017 The FOAM Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

foam.CLASS({
  package: 'foam.box',
  name: 'Runnable',

  requires: [ 'foam.box.Message' ],

  properties: [
    {
      class: 'FObjectProperty',
      of: 'foam.box.Box',
      documentation: 'Box to send to for computation output(s).',
      name: 'outputBox'
    },
    {
      class: 'String',
      name: 'ioRelationshipType',
      documentation: 'The n:m relationship type of input-to-output.',
      value: '1:1'
    }
  ],

  methods: [
    {
      name: 'run',
      documentation: 'Modeled computation for outputing to a box.',
      code: function() {}
    },
    function output(value) {
      this.outputBox.send(this.Message.create({
        object: value
      }));
    }
  ]
});
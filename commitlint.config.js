module.exports = {
  extends: ['gitmoji'],
  rules: {
    'type-empty': [0],
    'type-case': [0],
    'type-enum': [0],

    'subject-empty': [0],
    'subject-case': [0],

    'header-max-length': [0],

    'start-with-gitmoji': [2, 'always'],
  },
};

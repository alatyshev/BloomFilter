### Bloom Filter Kata exercise

Basic Bloom filter implementation according to this kata exercise: http://codekata.com/kata/kata05-bloom-filters

Supports add, check operations and calculation of false positive probability based on filter parameters.

- **add** - adds element to Bloom filter.
- **check** - returns true when element _might be_ in the set and false when element _definetely_ not in the set
- **calculateFalsePositiveProbability** - calculates probability using formalae: (1 - e^(-k * n / m)) ^ k, where 'k' is number of hashes, 'n' is number of expected elements in the set and 'm' is filter size.

Input string's MD5 hash digest is used for hashing, its byte array is then split into chunks and converted to integer numbers, which are used as an indices for adding and checking element in the filter.

####Testing
As Bloom Filter checks may return false positives, we are calculating false positive probability using input parameters for Bloom filter and asserting it against actual false positive rate (to make sure they are false positives we are doing additional check in actual input file). Test is parametrized, parameters taken from CSV file to testing with different Bloom filter parameters.
This test is slow as there are multiple parameters and large test dataset. There are also couple of smaller tests which are fast.   
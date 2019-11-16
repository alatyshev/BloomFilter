### Bloom Filter Kata exercise

Basic Bloom filter implementation according to this kata exercise: http://codekata.com/kata/kata05-bloom-filters

Supports add, check operations and calculation of false positive probability based on filter parameters.

- **add** - adds element to Bloom filter.
- **check** - returns true when element _might be_ in the set and false when element _definetely_ not in the set
- **calculateFalsePositiveProbability** - calculates probability using formulae: (1 - e^(-k * n / m)) ^ k, where 'k' is number of hashes, 'n' is number of expected elements in the set and 'm' is filter size.

Input string's MD5 hash digest is used for hashing, its byte array is then split into chunks and converted to integer numbers, which are used as an indices for adding and checking element in the filter.

#### Testing
As Bloom Filter checks may return false positives, we are calculating false positive probability using input parameters for Bloom filter and comparing it with actual false positive rate (to make sure they are false positives, we are doing additional check in actual input file). 
Test is parametrized, parameters taken from CSV file to testing with different Bloom filter parameters.
This test is slow as there are multiple parameters and large test dataset. Number of random 5 chars strings that are testing the filter could be adjusted in 'bloom-filter-test-parameters.csv' file (default is 100 for the sake of speedy test execution).

Example of test output:
```
Test run with: numberOfHashes=2, filterSize= 200000
===================================================
False Positives Expected Probability: 0.9336420064428124
False positives count: 99 out of 100 probes
False Positives Actual Rate: 0.99

Test run with: numberOfHashes=4, filterSize= 200000
===================================================
False Positives Expected Probability: 0.9954519387710727
False positives count: 98 out of 100 probes
False Positives Actual Rate: 0.98

Test run with: numberOfHashes=2, filterSize= 1000000
===================================================
False Positives Expected Probability: 0.2423090234888949
False positives count: 27 out of 100 probes
False Positives Actual Rate: 0.27

Test run with: numberOfHashes=4, filterSize= 1000000
===================================================
False Positives Expected Probability: 0.30342945658410636
False positives count: 26 out of 100 probes
False Positives Actual Rate: 0.26

Test run with: numberOfHashes=2, filterSize= 10000000
===================================================
False Positives Expected Probability: 0.004294259854060473
False positives count: 1 out of 100 probes
False Positives Actual Rate: 0.01

Test run with: numberOfHashes=4, filterSize= 10000000
===================================================
False Positives Expected Probability: 2.5824034716819796E-4
False positives count: 0 out of 100 probes
False Positives Actual Rate: 0.0
```


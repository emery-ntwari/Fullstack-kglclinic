function getOddNumbers(numbers) {
    return numbers.filter(number => number % 2 !== 0);
}

function multiplyOddNumbers(oddNumbers) {
    return oddNumbers.reduce((product, number) => product * number, 1);
}

console.log("This is array of odd numbers: " + getOddNumbers([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]));
const oddNumber = getOddNumbers([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]);
console.log("This is the product of odd numbers: " + multiplyOddNumbers(oddNumber));
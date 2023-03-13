const { expect } = require("chai");
const hre = require("hardhat");
const { time } = require("@nomicfoundation/hardhat-network-helpers");

describe("transfer function:", function () {
  it("Should be true", async function () {
    const StakingToken = await ethers.getContractFactory("StakingToken");
    const stakingToken = await StakingToken.deploy();
    expect(await stakingToken.transfer("0xf5d3B5b7bd6F310Ee692509dCB2933A5E1591799",100)).to.be.ok;
  });
});

describe("approve function:", function () {
  it("Should be true", async function () {
    const StakingToken = await ethers.getContractFactory("StakingToken");
    const stakingToken = await StakingToken.deploy();
    expect(await stakingToken.approve("0xf5d3B5b7bd6F310Ee692509dCB2933A5E1591799",100)).to.be.ok;
  });
});

const hre = require("hardhat");

async function main() {
  const [deployer] = await ethers.getSigners();
  const currentTimestampInSeconds = Math.round(Date.now() / 1000);
  const unlockTime = currentTimestampInSeconds + 60;

  console.log("Deploying StakingToken with the account:", deployer.address);
  const StakingToken = await ethers.getContractFactory("StakingToken");
  const stakingToken = await StakingToken.deploy();
  //await stakingToken.deployed();
    console.log(
      `StakingToken deployed to: ${stakingToken.address}`
    );
  console.log("Deploying RewardToken with the account:", deployer.address);
  const RewardToken = await ethers.getContractFactory("RewardToken");
  const rewardToken = await RewardToken.deploy();
  //await rewardToken.deployed();
    console.log(
      `RewardToken deployed to: ${rewardToken.address}`
    );

  console.log("Deploying MainToken with the account:", deployer.address);
  const MainToken = await ethers.getContractFactory("MainToken");
  const mainToken = await MainToken.deploy(rewardToken.address,stakingToken.address);
  //await mainToken.deployed();
    console.log(
      `MainToken deployed to: ${mainToken.address}`
    );

}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
// SPDX-License-Identifier: GPL-3.0

pragma solidity ^0.8.4;

import "./StakingToken.sol";
import "./RewardToken.sol";

contract MainToken {
    string public name = "MainToken";
    address public owner;
    StakingToken public stakingToken;
    RewardToken public rewardToken;
    address [] public stakers;
    mapping (address=> uint256) public stakingBalance;
    mapping (address=> bool) public hasStaked;
    mapping (address=> bool) public isStaking;

    constructor(RewardToken _rewardToken, StakingToken _stakingToken){
        rewardToken = _rewardToken;
        stakingToken= _stakingToken;
        owner = msg.sender;
    }

    function stakeTokens(uint _amount) public {
    require(_amount>0, "The amount must be more than 0");
    stakingToken.transferFrom(msg.sender, address(this), _amount);
    stakingBalance[msg.sender] += _amount;
    if (!hasStaked[msg.sender]){
        stakers.push(msg.sender);
    }
    isStaking[msg.sender] = true;
    hasStaked[msg.sender] = true;
    }


    function unstakeTokens() public
    {
        uint balance = stakingBalance[msg.sender];
        require(balance>0, "The balance must be more tha 0");

        stakingToken.transfer(msg.sender, balance);
        stakingBalance[msg.sender]=0;
        isStaking[msg.sender]= false;
    }

    function issueTokens() public{

        require(msg.sender == owner, "You are not the admin");

        for (uint i=0; i<stakers.length;i++ ){
            address recipient = stakers[i];
            uint balance = stakingBalance[recipient];
            if(balance>0){
                rewardToken.transfer(recipient, balance);
            }
        }
    }

}
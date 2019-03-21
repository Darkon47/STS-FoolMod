package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.subscribers.OnBattleEndSubscriber;

public class Greed extends AnimatorCard implements OnBattleEndSubscriber
{
    public static final String ID = CreateFullID(Greed.class.getSimpleName());

    public Greed()
    {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 3);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, this.magicNumber), this.magicNumber);
        GameActionsHelper.ApplyPower(p, p, new MalleablePower(p, this.magicNumber), this.magicNumber);

        for (OnBattleEndSubscriber s : PlayerStatistics.onBattleEnd.GetSubscribers())
        {
            if (s instanceof Greed)
            {
                return;
            }
        }
        PlayerStatistics.onBattleEnd.Subscribe(this);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
            //upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnBattleEnd()
    {
        int gold = (int)((1 - AbstractDungeon.player.currentHealth / (float)AbstractDungeon.player.maxHealth) * 100);
        logger.info("Gaining " + gold + " gold.");
        if (gold > 0)
        {
            AbstractRoom room = PlayerStatistics.GetCurrentRoom();
            if (room != null && room.rewardAllowed)
            {
                room.rewards.add(0, new SpecialGoldReward(this.originalName, gold));
                //room.addGoldToRewards(gold);
            }
        }
    }
}
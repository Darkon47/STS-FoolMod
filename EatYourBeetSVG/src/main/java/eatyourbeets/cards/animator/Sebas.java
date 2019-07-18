package eatyourbeets.cards.animator;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Sebas extends AnimatorCard
{
    public static final String ID = CreateFullID(Sebas.class.getSimpleName());

    public Sebas()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,9);

        this.exhaust = true;

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        int tmp = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group)
        {
            if (c.costForTurn > 0)// && !uuid.equals(c.uuid))
            {
                tmp += c.costForTurn;
            }
        }

        secondaryValue = tmp;
        isSecondaryValueModified = secondaryValue > 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.GainTemporaryHP(p, p, secondaryValue);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }
}
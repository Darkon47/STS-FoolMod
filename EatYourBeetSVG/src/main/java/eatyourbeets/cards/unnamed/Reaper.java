package eatyourbeets.cards.unnamed;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import eatyourbeets.actions.animator.KillCharacterAction;
import eatyourbeets.actions.common.IncreaseMaxHpAction;
import eatyourbeets.cards.UnnamedCard;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.GameActionsHelper;

public class Reaper extends UnnamedCard
{
    public static final String ID = CreateFullID(Reaper.class.getSimpleName());

    public Reaper()
    {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL, true);

        Initialize(0,0, 2, 30);

        AddExtendedDescription();

        this.tags.add(CardTags.HEALING);

        this.exhaust = true;
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        this.secondaryValue = this.baseSecondaryValue + PlayerStatistics.GetStrength(AbstractDungeon.player) * 3;
        this.isSecondaryValueModified = (this.secondaryValue != this.baseSecondaryValue);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m1.currentHealth < secondaryValue)
            {
                GameActionsHelper.AddToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
                GameActionsHelper.AddToBottom(new VFXAction(new CollectorCurseEffect(m1.hb.cX, m1.hb.cY), 1.0F));

                GameActionsHelper.AddToBottom(new KillCharacterAction(p, m));
                GameActionsHelper.AddToBottom(new IncreaseMaxHpAction(p, magicNumber, true));
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeSecondaryValue(10);
        }
    }
}
package eatyourbeets.cards.animator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;
import eatyourbeets.effects.SmallLaser2Effect;
import eatyourbeets.powers.PlayerStatistics;

public class Aisha extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Aisha.class.getSimpleName(), EYBCardBadge.Synergy);

    public Aisha()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(1, 0, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + Spellcaster.GetScaling());
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        int max = p.filledOrbCount();
        for (int i = 0; i < max; i++)
        {
             GameActionsHelper.VFX(new SmallLaser2Effect(p.hb.cX, p.hb.cY,
                    m.hb.cX + MathUtils.random(-0.05F, 0.05F),
                    m.hb.cY + MathUtils.random(-0.05F, 0.05F),
                    Color.VIOLET), 0.1F);

             GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.FIRE, true);
        }

        if (HasActiveSynergy() && PlayerStatistics.TryActivateLimited(cardID))
        {
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(2);
        }
    }
}
package eatyourbeets.cards.animator.colorless.rare;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.TanyaDegurechaff_Type95;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class TanyaDegurechaff extends AnimatorCard implements StartupCard
{
    public static final EYBCardData DATA = Register(TanyaDegurechaff.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Ranged).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.InitializePreview(new TanyaDegurechaff_Type95(), false);
    }

    public TanyaDegurechaff()
    {
        super(DATA);

        Initialize(4, 7);
        SetUpgrade(3, 0);

        SetSynergy(Synergies.YoujoSenki);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.DiscardFromHand(name, 999, true)
        .SetOptions(false, true, true)
        .SetFilter(c -> c.type == CardType.SKILL)
        .AddCallback(m, (enemy, cards) ->
        {
            for (int i = 0; i < cards.size(); i++)
            {
                GameActions.Bottom.SFX("ATTACK_FIRE");
                GameActions.Bottom.DealDamage(this, (AbstractCreature) enemy, AbstractGameAction.AttackEffect.NONE);
            }
        });
    }

    @Override
    public boolean atBattleStartPreDraw()
    {
        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActions.Bottom.MakeCardInDrawPile(new TanyaDegurechaff_Type95());

            return true;
        }

        return false;
    }
}
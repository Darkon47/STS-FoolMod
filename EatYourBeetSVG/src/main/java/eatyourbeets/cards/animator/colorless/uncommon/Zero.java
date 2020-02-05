package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class Zero extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Zero.class).SetSkill(0, CardRarity.UNCOMMON).SetColor(CardColor.COLORLESS);

    public Zero()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetExhaust(true);
        SetSynergy(Synergies.GrimoireOfZero);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractCard skill = JavaUtilities.GetRandomElement(p.drawPile.getSkills().group);
        if (skill != null)
        {
            p.drawPile.removeCard(skill);
            p.drawPile.addToTop(skill);
            skill.applyPowers();

            GameActions.Top.Add(new PlayTopCardAction(m, false));
        }
    }
}
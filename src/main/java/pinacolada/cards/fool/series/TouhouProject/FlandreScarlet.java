package pinacolada.cards.fool.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardEffectChoice;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.base.baseeffects.BaseEffect;
import pinacolada.cards.fool.FoolCard;
import pinacolada.cards.fool.special.FlandreScarlet_RemiliaScarlet;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

public class FlandreScarlet extends FoolCard
{
    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final PCLCardData DATA = Register(FlandreScarlet.class)
            .SetAttack(1, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new FlandreScarlet_RemiliaScarlet(), false));

    public FlandreScarlet()
    {
        super(DATA);

        Initialize(4, 0);
        SetUpgrade(2, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(1);

        SetAffinityRequirement(PCLAffinity.Dark, 8);

        SetEthereal(true);
    }

    public AbstractAttribute GetSpecialInfo() {
        return damage > 0 ? TempHPAttribute.Instance.SetCard(this).SetText(damage, Settings.CREAM_COLOR) : null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BITE);
        if (damage > 0)
        {
            PCLActions.Bottom.GainTemporaryHP(damage);
        }

        PCLActions.Last.Callback(() -> {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractCard handCard = PCLJUtils.Random(player.hand.group);

                choices.Initialize(this, true);
                choices.AddEffect(new BaseEffect_FlandreScarlet(0, this));
                if (handCard != null) {
                    choices.AddEffect(new BaseEffect_FlandreScarlet(1, handCard));
                }
                choices.Select(1, null);
            }
        });

        PCLActions.Last.Callback(() -> {
            if (CheckAffinity(PCLAffinity.Dark) && info.TryActivateLimited()) {
                PCLActions.Bottom.MakeCardInDrawPile(new FlandreScarlet_RemiliaScarlet());
            }
        });
    }

    protected static class BaseEffect_FlandreScarlet extends BaseEffect
    {
        private final AbstractCard target;
        public BaseEffect_FlandreScarlet(int amount, AbstractCard target)
        {
            this.amount = amount;
            this.target = target;
        }

        @Override
        public String GetText()
        {
            return FlandreScarlet.DATA.Strings.EXTENDED_DESCRIPTION[amount];
        }

        @Override
        public void Use(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
        {
            PCLActions.Bottom.Exhaust(target);
        }
    }

}

